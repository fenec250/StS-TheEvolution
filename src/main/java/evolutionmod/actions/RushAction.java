package evolutionmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RushAction extends AbstractGameAction {

	private boolean playFake;

	public RushAction(AbstractPlayer player, AbstractMonster monster, boolean playFake) {
		this.source = player;
		this.target = monster;
		this.playFake = playFake;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DRAW;
	}

	public void update() {
		AbstractPlayer player = (AbstractPlayer) this.source;
		if (player.hasPower("No Draw")) {
			this.isDone = true;
			this.duration = 0.0F;
			this.actionType = ActionType.WAIT;
			return;
		}
		if (player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
			player.createHandIsFullDialog();
			this.isDone = true;
			return;
		}
		if (player.drawPile.isEmpty()) {
			this.isDone = true;
			return;
		}
		Optional<AbstractCard> strike = player.drawPile.group.stream()
				.filter(card -> card.hasTag(AbstractCard.CardTags.STRIKE)).collect(Collectors.collectingAndThen(
						Collectors.toList(),
						list -> list.size() > 0
								? Optional.of(list.get(AbstractDungeon.cardRng.random(list.size() - 1)))
								: Optional.empty()
						)
				);
		strike.ifPresent(card -> {
			if (playFake) {
				player.drawPile.moveToHand(card, player.drawPile);
				card = card.makeSameInstanceOf();
				card.purgeOnUse = true;
			}
			if (card.cost > 0) {
				card.freeToPlayOnce = true;
			}
			AbstractDungeon.player.limbo.group.add(card);
			card.current_y = -200.0F * Settings.scale;
			card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
			card.target_y = (float) Settings.HEIGHT / 2.0F;
			card.targetAngle = 0.0F;
			card.lighten(false);
			card.drawScale = 0.12F;
			card.targetDrawScale = 0.75F;
			AbstractMonster monster = (AbstractMonster) this.target;
			if (monster != null) {
				card.calculateCardDamage(monster);
			}
			card.applyPowers();
			AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
			AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
		});
		this.isDone = true;
		this.tickDuration();
	}
}

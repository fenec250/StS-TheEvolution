package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import evolutionmod.powers.MarkPower;

import java.util.stream.Collectors;

public class LavafolkGeneAction extends AbstractGameAction {

	private int damage;

	public LavafolkGeneAction(AbstractPlayer player, int damage) {
		this.source = player;
		this.damage = damage;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DAMAGE;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		AbstractMonster monster = AbstractDungeon.getMonsters().monsters.stream()
				.filter(m -> m.hasPower(MarkPower.POWER_ID) && !m.isDeadOrEscaped())
				.collect(Collectors.collectingAndThen(
						Collectors.toList(),
						list -> {
							if (list.isEmpty()) {
								return AbstractDungeon.getRandomMonster();
							}
							return list.get(AbstractDungeon.cardRng.random(list.size() - 1));
						}));
		AbstractDungeon.actionManager.addToTop(new DamageAction(
				monster, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS), AttackEffect.NONE, true));

		this.isDone = true;

		this.tickDuration();
	}
}

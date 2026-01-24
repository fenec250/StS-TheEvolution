package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.cards.Swampborn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LizardGeneAction extends AbstractHalfTargetedAction {

	private int poison;
	private int damage;

	public LizardGeneAction(AbstractPlayer player, AbstractMonster monster, int damage, int poison) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.poison = poison;
		this.damage = damage;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		List<AbstractCreature> targets = this.source.hasPower(Swampborn.SwampbornPower.POWER_ID)
				? AbstractDungeon.getMonsters().monsters.stream()
				.filter(m -> !m.isDeadOrEscaped())
				.map(m -> (AbstractCreature) m)
				.collect(Collectors.toList())
				: new ArrayList<AbstractCreature>() {{
			add(target);
		}};

		targets.forEach(c -> {
			if (damage > 0) {
				AbstractDungeon.actionManager.addToTop(new DamageAction(
						c, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS),
						AttackEffect.POISON, true));
			}
			AbstractDungeon.actionManager.addToTop(
					new ApplyPowerAction(c, this.source, new PoisonPower(c, this.source, this.poison), this.poison));
		});

		this.isDone = true;

		this.tickDuration();
	}
}

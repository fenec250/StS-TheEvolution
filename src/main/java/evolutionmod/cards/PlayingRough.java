package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LustPower;

public class PlayingRough
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PlayingRough";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Nightmare.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 11;
    private static final int UPGRADE_DAMAGE_AMT = 3;
	private static final int FORM_DAMAGE_AMT = 3;

    public PlayingRough() {
		super(ID, NAME, new RegionName("red/attack/uppercut"), COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlayingRoughAction(
                p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        formEffect(SuccubusGene.ID);
    }

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0) + (isPlayerInThisForm(SuccubusGene.ID) ? this.magicNumber : 0);
		supercall.run();
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0);
		this.isDamageModified = this.damage != this.baseDamage;
	}

    @Override
    public AbstractCard makeCopy() {
        return new PlayingRough();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(SuccubusGene.ID)) {
			this.glowColor = SuccubusGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public static class PlayingRoughAction extends AbstractGameAction {

		private DamageInfo info;

		public PlayingRoughAction(AbstractCreature source, AbstractCreature target, DamageInfo info, AttackEffect effect) {
			this.source = source;
			this.target = target;
			this.info = info;
			this.setValues(target, info);
			this.actionType = ActionType.DAMAGE;
			this.attackEffect = effect;
			this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		}

		public void update() {
			this.tickDuration();
			if (this.isDone) {
				this.target.damage(this.info);
				if (this.target.lastDamageTaken/2 > 0) {
					if (!target.isDying) {
						AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.source,
								new LustPower(this.target, this.target.lastDamageTaken/2)));
					}
				}
			}
		}
	}
}

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
import evolutionmod.powers.GrowthPower;
import evolutionmod.powers.LustPower;

public class VerdantStomp
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:VerdantStomp";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Nightmare.png";
    private static final int COST = 3;
    private static final int DAMAGE_AMT = 16;
    private static final int UPGRADE_DAMAGE_AMT = 5;
	private static final int CONVERSION_REFERENCE_AMT = 16;
	private static final int CONVERSION_GROWTH_AMT = 2;

    public VerdantStomp() {
		super(ID, NAME, new RegionName("red/attack/uppercut"), COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = CONVERSION_GROWTH_AMT;
		this.rawDescription = EXTENDED_DESCRIPTION[0] + CONVERSION_REFERENCE_AMT/this.magicNumber + EXTENDED_DESCRIPTION[1];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		int chunk = CONVERSION_REFERENCE_AMT/this.magicNumber;
        addToBot(new VerdantStompAction(
                p, m, chunk, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VerdantStomp();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

	public static class VerdantStompAction extends AbstractGameAction {

		private DamageInfo info;
		private int chunk;

		public VerdantStompAction(AbstractCreature source, AbstractCreature target, int chunk, DamageInfo info, AttackEffect effect) {
			this.source = source;
			this.target = target;
			this.chunk = chunk;
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
				int amount = this.target.lastDamageTaken/chunk;
				if (amount > 0) {
					addToTop(new ApplyPowerAction(this.source, this.source,
							new GrowthPower(this.target, amount), amount));
				}
			}
		}
	}
}

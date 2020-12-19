package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.MatureEggPower;

public class Drone
        extends AbstractDrone {
    public static final String ID = "evolutionmod:Drone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectAtt.png";
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int BLOCK_AMT = 1;
    private static final int UPGRADE_BLOCK_AMT = 2;

    public Drone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
        this.isEthereal = true;
    }

    public static Drone createDroneWithInteractions(AbstractPlayer player) {
        Drone drone = new Drone();
        if (player != null && player.hasPower(MatureEggPower.POWER_ID)) {
            AbstractPower eggs = player.getPower(MatureEggPower.POWER_ID);
            if (eggs.amount > 0) {
                drone.upgrade();
                eggs.stackPower(-1);
            }
        }
        return drone;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(m, this.block));
        addToBot(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void applyPowers() {
        applyBroodPowerAround(() -> removeStrAround(super::applyPowers));
    }

    @Override
    protected void applyPowersToBlock() {
        removeDexAround(() -> super.applyPowersToBlock());
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyBroodPowerAround(() -> removeStrAround(() -> super.calculateCardDamage(mo)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}